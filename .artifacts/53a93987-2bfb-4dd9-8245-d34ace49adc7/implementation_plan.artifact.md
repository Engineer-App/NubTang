# แผนการอัปเกรดระบบอัปเดตอัตโนมัติและความน่าเชื่อถือ (Final Plan)

เป้าหมายคือการทำให้แอป NabTang สามารถอัปเดตตัวเองได้โดยข้อมูลไม่หาย และเพิ่มความน่าเชื่อถือในระดับมืออาชีพ

## 1. ระบบกุญแจลายเซ็น (Permanent Signing)
- สร้างไฟล์ `release.keystore` ไว้ในโปรเจกต์
- ตั้งค่า `build.gradle.kts` ให้ใช้กุญแจนี้ในการสร้าง APK ทุกครั้ง เพื่อให้การติดตั้งทับของเดิม (Update) ทำได้โดยที่ Android ไม่บังคับให้ลบข้อมูลเก่า

## 2. ระบบตรวจสอบและติดตั้งแอป (In-App Update)
- **[NEW] [UpdateManager.kt]**: คลาสสำหรับดาวน์โหลด APK และเรียกใช้งานระบบติดตั้งของ Android
- **[MODIFY] [AndroidManifest.xml]**: เพิ่ม Permission `REQUEST_INSTALL_PACKAGES` และตั้งค่า `FileProvider` เพื่อแชร์ไฟล์ APK ให้ระบบติดตั้ง
- **[MODIFY] [SettingsScreen.kt]**: เพิ่มหน้าจอ "ตรวจสอบการอัปเดต" และแสดงข้อมูลเวอร์ชันปัจจุบัน

## 3. ความน่าเชื่อถือและ UI (Trust & Polishing)
- **[NEW] Splash Screen**: เพิ่มหน้าจอโลโก้ตอนเปิดแอป (Android 12+ Standard)
- **[MODIFY] LockScreen.kt**: เพิ่มระบบสั่น (Haptic Feedback) เมื่อกดรหัส PIN
- **[MODIFY] SettingsScreen.kt**: เพิ่มข้อความยืนยันนโยบายความเป็นส่วนตัว (Local Data Only)

---

## รายการไฟล์ที่จะแก้ไข

### [Core/Infrastructure]
- #### [MODIFY] [build.gradle.kts](file:///D:/Claude Code/Android App/app/build.gradle.kts)
- #### [MODIFY] [AndroidManifest.xml](file:///D:/Claude Code/Android App/app/src/main/AndroidManifest.xml)

### [Update System]
- #### [NEW] UpdateManager.kt (in `com.pft.tracker.util`)
- #### [NEW] UpdateStatus.kt (Data model)

### [UI/UX]
- #### [MODIFY] [SettingsScreen.kt](file:///D:/Claude Code/Android App/app/src/main/java/com/pft/tracker/ui/settings/SettingsScreen.kt)
- #### [MODIFY] [LockScreen.kt](file:///D:/Claude Code/Android App/app/src/main/java/com/pft/tracker/ui/lock/LockScreen.kt)
- #### [NEW] `res/xml/file_paths.xml` (สำหรับ FileProvider)

---

## การเตรียมการสำหรับผู้ใช้
หลังจากการแก้ไขนี้เสร็จสิ้น ผมจะให้ไฟล์ `version.json` เป็นตัวอย่างเพื่อให้คุณไปวางไว้ใน GitHub เพื่อให้แอปใช้เช็คเวอร์ชันครับ

**ตกลงตามแผนนี้ไหมครับ? หากตกลงผมจะเริ่มดำเนินการทันทีครับ**
