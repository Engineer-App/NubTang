# แอปบันทึกธุรกรรมทางการเงิน (Personal Finance Tracker)
## App Requirement + Database Schema + Screen Flow — สำหรับส่งต่อให้ Claude Code พัฒนา

> เอกสารนี้สรุปมาจาก concept ที่ทดสอบและยืนยันโครงสร้างข้อมูลผ่าน Excel prototype แล้ว
> เป้าหมาย: ให้ Claude Code เริ่มสร้างโปรเจกต์ Android **Version 1.0** ได้ทันทีโดยไม่ต้องถามกลับ

---

## 1. ภาพรวมแอป

แอปบันทึก**ธุรกรรมทางการเงิน** (ไม่ใช่แค่ค่าใช้จ่าย) เพราะต้องรองรับทั้งรายจ่าย รายรับ การโอนเงิน การถอนเงินสด และการชำระบัตรเครดิต หากแยกประเภทธุรกรรมตั้งแต่ต้น ยอดคงเหลือของทุกบัญชีจะถูกต้องเสมอและขยายฟีเจอร์ในอนาคตได้ง่าย

**Tech stack:** Kotlin, Jetpack Compose, Room (SQLite), ViewModel + Repository pattern, DataStore (ค่าตั้งค่าเล็ก ๆ), WorkManager (งานเบื้องหลัง/แจ้งเตือน), Material 3, Offline-first

---

## 2. หลักการบันทึกยอด (Core Accounting Logic — สำคัญที่สุด)

มีธุรกรรมหลัก 5 ประเภท (enum `TransactionType`):

| ประเภท | ผลต่อยอด |
|---|---|
| `EXPENSE` (รายจ่าย) | บัญชี/เงินสดต้นทางลดลง **หรือ** ถ้าจ่ายด้วยบัตรเครดิต → ยอดหนี้บัตรเพิ่มขึ้น (เงินในธนาคารยังไม่ลด) |
| `INCOME` (รายรับ) | บัญชีปลายทางเพิ่มขึ้น |
| `TRANSFER` (โอนเงิน) | บัญชีต้นทางลด บัญชีปลายทางเพิ่ม — **ไม่นับเป็นค่าใช้จ่าย** |
| `CASH_WITHDRAWAL` (ถอนเงินสด) | บัญชีธนาคารลด เงินสดเพิ่ม — **ไม่นับเป็นค่าใช้จ่าย** (ป้องกันนับซ้ำตอนใช้เงินสดจริง) |
| `CREDIT_CARD_PAYMENT` (ชำระบัตรเครดิต) | บัญชีธนาคารลด ยอดหนี้บัตรลด — **ไม่นับเป็นค่าใช้จ่ายซ้ำ** เพราะนับตอนรูดบัตรไปแล้ว |

**กฎการคำนวณยอดคงเหลือ (ต้องทำในฝั่ง Repository/UseCase ไม่ใช่ query กระจัดกระจาย):**
- ทุกการเปลี่ยนแปลงยอดต้องผ่าน `Transaction` เท่านั้น ห้ามแก้ `current_balance` ตรง ๆ จากที่อื่น
- การบันทึกธุรกรรมที่กระทบ 2 บัญชี (transfer, withdrawal, cc payment) ต้องอยู่ใน **Room `@Transaction`** เดียวกัน (atomic) — ถ้าฝั่งใดฝั่งหนึ่งล้มเหลวต้อง rollback ทั้งหมด

---

## 3. Database Schema (Room Entities)

```kotlin
// ---------- accounts (เงินสด + บัญชีธนาคาร) ----------
@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val accountType: String,          // "CASH" | "BANK"
    val bankName: String? = null,
    val accountNumberLast4: String? = null,
    val openingBalance: Double,
    val isActive: Boolean = true,
    val creditLimitGroupId: Long? = null,  // NEW: บัญชีไม่ใช้ฟิลด์นี้ (สำหรับบัตรเท่านั้น) — เว้นไว้เป็น null เสมอ
    val createdAt: Long = System.currentTimeMillis()
)

// ---------- credit_limit_groups (NEW: กลุ่มวงเงินร่วมของหลายบัตร) ----------
@Entity(tableName = "credit_limit_groups")
data class CreditLimitGroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,                 // เช่น "วงเงินร่วมบัตรกสิกร (หลัก+เสริม)"
    val sharedLimit: Double
)

// ---------- credit_cards ----------
@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val issuer: String,
    val cardNumberLast4: String,
    val creditLimit: Double,          // ใช้เมื่อ creditLimitGroupId == null (วงเงินของตัวเอง)
    val creditLimitGroupId: Long? = null,  // ถ้าไม่ null = ใช้วงเงินร่วมจากกลุ่มแทน ignoring creditLimit
    val billingFrequencyMonths: Int = 1,   // ตัดทุกกี่เดือน (1 = ทุกเดือน)
    val statementDay: Int,            // วันตัดรอบ 1-31
    val paymentDueDay: Int,           // วันครบกำหนดชำระ (ของเดือนถัดจากวันตัด)
    val startMonth: Int? = null,      // ใช้เมื่อ billingFrequencyMonths > 1 เพื่อ anchor รอบ
    val isActive: Boolean = true
)

// ---------- categories ----------
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val categoryType: String,         // "EXPENSE" | "INCOME"
    val parentCategoryId: Long? = null,
    val icon: String? = null,
    val monthlyBudget: Double? = null, // NEW: null หรือ 0 = ไม่จำกัดงบ
    val displayOrder: Int = 0
)

// ---------- transactions (ตารางหลัก) ----------
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionDate: Long,        // epoch millis (เก็บเวลาด้วยเพื่อ sort ในวันเดียวกัน)
    val transactionType: String,      // EXPENSE / INCOME / TRANSFER / CASH_WITHDRAWAL / CREDIT_CARD_PAYMENT
    val title: String,
    val categoryId: Long? = null,
    val amount: Double,
    val sourceAccountId: Long? = null,     // FK -> accounts.id  (เงินสด/ธนาคาร/หรือใช้ sourceCreditCardId แทนถ้าจ่ายด้วยบัตร)
    val sourceCreditCardId: Long? = null,  // FK -> credit_cards.id (กรณี EXPENSE ที่จ่ายด้วยบัตร)
    val destinationAccountId: Long? = null,    // FK -> accounts.id (transfer/withdrawal ปลายทาง)
    val destinationCreditCardId: Long? = null, // FK -> credit_cards.id (cc payment: บัตรที่ถูกชำระ)
    val note: String? = null,
    val receiptPath: String? = null,
    val isRecurringGenerated: Boolean = false, // NEW: true ถ้าเกิดจากระบบผ่อน/รายการประจำอัตโนมัติ
    val recurringPlanId: Long? = null,         // NEW: อ้างอิงกลับไปที่แผน (ถ้ามี)
    val creditCardStatementId: Long? = null,   // คำนวณ/ผูกภายหลังจาก billing cycle logic
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// ---------- credit_card_statements (รอบบิลของแต่ละบัตร) ----------
@Entity(tableName = "credit_card_statements")
data class CreditCardStatementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creditCardId: Long,
    val periodStart: Long,
    val periodEnd: Long,              // = วันตัดรอบของงวดนี้
    val statementDate: Long,
    val dueDate: Long,
    val statementAmount: Double,
    val paidAmount: Double = 0.0,
    val status: String                // NOT_YET_BILLED / BILLED / PARTIALLY_PAID / PAID / OVERDUE
)

// ---------- recurring_transactions (ผ่อนชำระ / รายการประจำ) ----------
@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,                     // เช่น "ผ่อนโทรศัพท์มือถือ"
    val amount: Double,
    val categoryId: Long? = null,
    val sourceAccountId: Long? = null,
    val sourceCreditCardId: Long? = null,
    val startDate: Long,
    val frequency: String,                // MONTHLY / WEEKLY / YEARLY / ONE_TIME
    val totalInstallments: Int? = null,   // null = ไม่มีกำหนดสิ้นสุด (รายการประจำถาวร เช่น ค่าเช่า)
    val installmentsGenerated: Int = 0,   // นับจำนวนงวดที่ auto-insert ไปแล้ว
    val nextRunDate: Long,
    val endDate: Long? = null,
    val note: String? = null,
    val isActive: Boolean = true
)
```

**ความสัมพันธ์สำคัญ:** `sourceAccountId` กับ `sourceCreditCardId` เป็น mutually exclusive (มีได้แค่ 1 ค่า) — validate ที่ชั้น UI/UseCase ก่อนบันทึกเสมอ เช่นเดียวกับ `destinationAccountId` / `destinationCreditCardId`

---

## 4. Business Logic ที่ต้องทำ (ระดับ Repository / UseCase)

### 4.1 คำนวณยอดคงเหลือบัญชี (`AccountBalanceUseCase`)
```
currentBalance(account) = openingBalance
  + SUM(amount WHERE type=INCOME AND sourceAccountId=account)
  + SUM(amount WHERE type IN (TRANSFER, CASH_WITHDRAWAL) AND destinationAccountId=account)
  - SUM(amount WHERE type=EXPENSE AND sourceAccountId=account)
  - SUM(amount WHERE type IN (TRANSFER, CASH_WITHDRAWAL, CREDIT_CARD_PAYMENT) AND sourceAccountId=account)
```

### 4.2 คำนวณยอดหนี้บัตรเครดิต (`CreditCardBalanceUseCase`)
```
currentUsed(card) = SUM(amount WHERE type=EXPENSE AND sourceCreditCardId=card)
                   - SUM(amount WHERE type=CREDIT_CARD_PAYMENT AND destinationCreditCardId=card)

availableLimit(card) =
   if card.creditLimitGroupId == null:
       card.creditLimit - currentUsed(card)
   else:
       group.sharedLimit - SUM(currentUsed(c) for c in cards where c.creditLimitGroupId == group.id)
```
> บัตรที่แชร์วงเงินกลุ่มเดียวกันจะแสดง "วงเงินคงเหลือ" เท่ากันทุกใบ (คำนวณจากยอดรวมการใช้ของทุกบัตรในกลุ่ม) — แสดง badge "วงเงินร่วมกับ [ชื่อบัตรอื่น]" ในหน้ารายละเอียดบัตรด้วย

### 4.3 คำนวณรอบบิลที่ธุรกรรมสังกัด (`BillingCycleUseCase`)
สำหรับธุรกรรมที่จ่ายด้วยบัตรเครดิต ให้หาว่าธุรกรรมนั้นอยู่ในรอบบิลไหน:
```
fun statementCutDate(card, transactionDate): LocalDate {
    val cutoffDay = card.statementDay
    val thisMonthCutoff = transactionDate.withDayOfMonth(min(cutoffDay, transactionDate.lengthOfMonth()))
    return if (transactionDate.dayOfMonth <= cutoffDay) thisMonthCutoff
           else thisMonthCutoff.plusMonths(card.billingFrequencyMonths.toLong())
}
```
- ตัวอย่าง: บัตรตัดรอบวันที่ 19 ทุกเดือน → ธุรกรรมวันที่ 21 จะถูกจัดเข้ารอบบิลของเดือนถัดไปโดยอัตโนมัติ
- ถ้า `billingFrequencyMonths > 1` (เช่น ตัดทุก 2 เดือน) ให้ anchor รอบจาก `startMonth` ที่ตั้งไว้ในบัตร
- ใช้ค่านี้ผูก `creditCardStatementId` ตอนบันทึกธุรกรรม หรือคำนวณ on-the-fly ตอน query ก็ได้ (แนะนำ: คำนวณ on-the-fly ด้วย SQL/Kotlin เพื่อความยืดหยุ่น ไม่ต้อง migrate ข้อมูลเก่าเวลาผู้ใช้แก้วันตัดรอบ)

### 4.4 งบประมาณต่อหมวด (`BudgetUseCase`)
```
spent(category, month) = SUM(amount WHERE type=EXPENSE AND categoryId=category AND month(transactionDate)=month)
overBudget = category.monthlyBudget != null && category.monthlyBudget > 0 && spent > category.monthlyBudget
overAmount = max(spent - (category.monthlyBudget ?: 0.0), 0.0)
```
- กราฟแท่งใน Dashboard: แสดง 2 ส่วนซ้อนกัน (stacked) — ส่วน "ภายในงบ" (สีปกติ) + ส่วน "เกินงบ" (สีแดง) แบบเดียวกับที่ทดสอบใน Excel prototype

### 4.5 รายการล่วงหน้า/ผ่อนชำระ (`RecurringTransactionWorker` — ใช้ WorkManager)
- Worker รันทุกวัน (`PeriodicWorkRequest`, ~00:05 น.) เช็คทุกแผนใน `recurring_transactions` ที่ `isActive=true`
- ถ้า `nextRunDate <= today` → สร้าง `TransactionEntity` ใหม่อัตโนมัติ (type=EXPENSE, `isRecurringGenerated=true`, `recurringPlanId` ผูกกลับ) แล้วอัปเดต `installmentsGenerated += 1` และเลื่อน `nextRunDate` ตาม `frequency`
- ถ้า `totalInstallments` ไม่ใช่ null และ `installmentsGenerated >= totalInstallments` → ตั้ง `isActive=false` (จบแผน)
- ส่ง Notification แจ้งผู้ใช้ทุกครั้งที่สร้างรายการอัตโนมัติ ("บันทึกรายการ 'ผ่อนโทรศัพท์มือถือ' งวดที่ 3/10 ให้อัตโนมัติแล้ว")
- **นี่คือจุดที่ Android ทำได้ดีกว่า Excel มาก** เพราะทำงานเบื้องหลังได้จริงแม้ไม่เปิดแอป ไม่ต้องพึ่งการเปิดไฟล์หรือมาโคร

### 4.6 กรองรายการตามเดือน (หน้า Transactions List)
- ใช้ Room DAO query ธรรมดา `WHERE transactionDate BETWEEN :monthStart AND :monthEnd` ผูกกับ `StateFlow<LocalDate>` ของเดือนที่เลือก
- เปลี่ยนเดือน → emit ค่าใหม่ → Flow re-query อัตโนมัติ → UI recompose ทันที (ไม่ต้องกดปุ่ม reapply เหมือนใน Excel)
- ข้อมูลเดือนอื่นไม่หายไปไหน อยู่ใน DB ครบ แค่ query ไม่ได้ดึงมาแสดง — สลับกลับไปเดือนเดิมก็เห็นครบเหมือนเดิมทันที

---

## 5. หน้าจอ (Screens) และ Flow

**Bottom navigation (5 เมนู):** Dashboard | รายการ | (ปุ่มเพิ่มรายการ เด่นกลาง) | บัญชี | ตั้งค่า

### 5.1 Dashboard
- ตัวเลือกช่วงเวลา (เดือนนี้ / เดือนก่อน / ปีนี้ / กำหนดเอง)
- การ์ดสรุป: ค่าใช้จ่ายรวม, รายรับรวม, เงินสดคงเหลือ, เงินในบัญชีรวม, ยอดใช้บัตรเครดิตค้างชำระ, ยอดคงเหลือสุทธิ
- กราฟแท่ง: ค่าใช้จ่ายเทียบงบประมาณต่อหมวด (สีแดง = เกินงบ)
- กราฟวงกลม: ค่าใช้จ่ายแยกตามบัญชี/บัตร
- เปรียบเทียบเดือนนี้กับเดือนก่อน

### 5.2 รายการ (Transaction List)
- ตัวเลือกเดือน (chip/dropdown ด้านบน) — กรองแบบ reactive ตาม §4.6
- Filter เพิ่มเติม: ประเภทธุรกรรม, หมวดหมู่, บัญชี/บัตร
- แต่ละแถวแตะเพื่อแก้ไข/ลบ; swipe เพื่อลบเร็ว
- รายการที่เกิดจากระบบผ่อนอัตโนมัติ (`isRecurringGenerated=true`) มี badge เล็ก ๆ กำกับ "ผ่อนอัตโนมัติ"

### 5.3 เพิ่ม/แก้ไขธุรกรรม
- เลือกประเภทธุรกรรมก่อน (5 ปุ่มใหญ่) → ฟอร์มปรับตามประเภท (transfer/withdrawal/cc payment โชว์ 2 บัญชี, expense/income โชว์บัญชีเดียว)
- วันที่, รายการ, หมวดหมู่ (เฉพาะ expense/income), จำนวนเงิน, บัญชี/บัตร, หมายเหตุ, แนบรูปใบเสร็จ

### 5.4 บัญชีและเงินสด
- List การ์ดบัญชี พร้อมยอดคงเหลือปัจจุบัน, ปุ่มเพิ่มบัญชีใหม่
- แตะเพื่อดูรายละเอียด/ประวัติธุรกรรมของบัญชีนั้น

### 5.5 บัตรเครดิต
- List การ์ดบัตร: วงเงิน, ยอดใช้, วงเงินคงเหลือ (ถ้าอยู่กลุ่มแชร์วงเงิน แสดง badge), วันตัดรอบ, วันครบกำหนด, สถานะ
- หน้าตั้งค่ากลุ่มวงเงินร่วม (สร้าง/แก้กลุ่ม, เลือกบัตรที่เข้าร่วม)
- แตะบัตร → ดูรอบบิลแต่ละงวด (`credit_card_statements`) พร้อมยอด/สถานะ

### 5.6 หมวดหมู่และงบประมาณ
- List หมวดหมู่ พร้อมช่องตั้งงบต่อเดือน, เพิ่ม/แก้/ลบหมวดได้

### 5.7 รายการล่วงหน้า/ผ่อนชำระ
- List แผนที่ตั้งไว้ (ชื่อ, จำนวนเงิน/งวด, ความคืบหน้า เช่น "งวดที่ 3/10")
- เพิ่มแผนใหม่: ชื่อ, จำนวนเงิน, หมวดหมู่, บัญชี/บัตร, วันที่เริ่ม, ความถี่, จำนวนงวด (หรือไม่มีกำหนด)
- ปิด/หยุดแผนได้ตลอดเวลา

### 5.8 ตั้งค่า
- PIN/ลายนิ้วมือ, Auto-lock, สำรอง/กู้คืนข้อมูล, ส่งออก CSV/Excel, เกี่ยวกับแอป

---

## 6. ขอบเขต Version 1.0

**รวมอยู่ใน v1.0:**
- ธุรกรรมครบ 5 ประเภท + เพิ่ม/แก้/ลบ
- บัญชีเงินสด/ธนาคาร + บัตรเครดิต (รวมกลุ่มวงเงินร่วม)
- หมวดหมู่ + งบประมาณต่อเดือน + แจ้งเตือนเกินงบ (สีแดงในกราฟ)
- รอบบัตรเครดิตตามวันตัดรอบ (รายเดือน/ทุก N เดือน/กำหนดเอง)
- รายการล่วงหน้า/ผ่อนชำระ พร้อม auto-generate ผ่าน WorkManager
- Dashboard รายเดือน + กราฟ + กรองตามประเภท/บัญชี/บัตร
- สำรอง/กู้คืนข้อมูล, ส่งออก CSV/Excel

**เก็บไว้ v1.1–2.0:** สแกนใบเสร็จ, อ่าน SMS ธนาคาร, นำเข้า statement, sync หลายเครื่อง, Google Drive backup, หลายสกุลเงิน, บัญชีครอบครัว/สมาชิกหลายคน

---

## 7. ความปลอดภัย
- PIN หรือ biometric authentication ก่อนเข้าแอป
- เก็บเฉพาะชื่อบัตร+เลขท้าย 4 หลัก ห้ามเก็บเลขบัตรเต็ม
- เข้ารหัสไฟล์สำรอง (backup)
- Auto-lock เมื่อออกจากแอป, ซ่อนยอดเงินในหน้ารวมแอปล่าสุด (recent apps preview)
- สำรองข้อมูลก่อน migrate schema ทุกครั้ง (Room `Migration`)

---

## 8. ข้อมูลตัวอย่างสำหรับทดสอบ (Seed data)
ใช้ชุดข้อมูลเดียวกับที่ทดสอบใน Excel prototype ได้เลย (2 บัญชีธนาคาร + เงินสด, 2 บัตรเครดิต, ~20 ธุรกรรมเดือนกรกฎาคม 2026, 1 แผนผ่อนชำระ) เพื่อ verify ว่า logic การคำนวณยอด/รอบบัตร/งบประมาณตรงกับที่ยืนยันไว้แล้วใน spreadsheet

---

### หมายเหตุถึง Claude Code
โครงสร้างและ business logic ทั้งหมดในเอกสารนี้ผ่านการทดสอบเชิงตัวเลขแล้วใน Excel prototype (สูตร SUMIFS/EDATE ทุกจุด verify ผลลัพธ์ถูกต้อง) จึงใช้เป็น source of truth ได้ทันที ไม่ต้องออกแบบใหม่ — เริ่มจากสร้าง Room entities + DAO ตาม §3, ตามด้วย UseCase ตาม §4, แล้วค่อยต่อ UI ตาม §5
