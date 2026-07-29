# ขั้นตอนการอัปโหลดแอป NabTang ขึ้น GitHub

เพื่อให้ระบบอัปเดตอัตโนมัติทำงานได้ คุณต้องนำโปรเจกต์ขึ้น GitHub และวางไฟล์เวอร์ชันไว้ในที่ที่แอปสามารถอ่านได้ครับ

## 1. เตรียมตัวบน GitHub
1. เข้าไปที่ [GitHub.com](https://github.com/) และสร้าง **New Repository**
2. ตั้งชื่อว่า `NabTang` (หรือชื่อที่คุณต้องการ)
3. เลือกเป็น **Public** (เพื่อให้แอปอ่านไฟล์เวอร์ชันได้โดยไม่ต้อง Login)
4. กด **Create repository**

## 2. อัปโหลดโค้ดผ่าน Terminal ใน Android Studio
เปิด Terminal ใน Android Studio (แถบด้านล่าง) แล้วพิมพ์คำสั่งตามลำดับดังนี้ครับ:

```bash
# 1. เริ่มต้นระบบ Git
git init

# 2. เลือกไฟล์ทั้งหมดเตรียมอัปโหลด
git add .

# 3. บันทึกประวัติการแก้ไขครั้งแรก
git commit -m "Initial commit with Update System and Splash Screen"

# 4. เชื่อมต่อกับ GitHub (แทนที่ USERNAME ด้วยชื่อของคุณ)
git branch -M main
git remote add origin https://github.com/USERNAME/NabTang.git

# 5. ส่งไฟล์ขึ้น GitHub
git push -u origin main
```

## 3. การวางไฟล์เพื่อให้แอปเช็คอัปเดต
เพื่อให้ระบบ "ตรวจสอบการอัปเดต" ทำงานได้จริง คุณต้องมี 2 ส่วนนี้ครับ:

### ก. วางไฟล์ version.json
1. ในหน้า GitHub ของคุณ ให้กดปุ่ม **Add file > Create new file**
2. ตั้งชื่อว่า `version.json`
3. คัดลอกเนื้อหาจากไฟล์ `version.json` ในเครื่องไปวาง
4. กด **Commit changes**
5. **สำคัญ:** กดที่ไฟล์ `version.json` บน GitHub แล้วกดปุ่ม **Raw** คุณจะได้ลิงก์ยาวๆ (เช่น `https://raw.githubusercontent.com/...`) ให้นำลิงก์นี้ไปใส่ใน `SettingsViewModel.kt` บรรทัดที่ 61 ครับ

### ข. อัปโหลด APK (ทำเมื่อต้องการแจกจ่ายเวอร์ชันใหม่)
1. ในหน้าแรกของ Repository บน GitHub มองหาเมนู **Releases** ทางด้านขวา
2. กด **Create a new release**
3. ตั้งชื่อ Tag เช่น `v1.1`
4. ลากไฟล์ `app-debug.apk` มาวางในช่อง Assets
5. กด **Publish release**
6. คลิกขวาที่ปุ่มดาวน์โหลดไฟล์ APK ที่อัปโหลดไป แล้วเลือก **Copy link address** เพื่อนำลิงก์ไปใส่ใน `downloadUrl` ของไฟล์ `version.json` ครับ

> [!TIP]
> **สรุปหัวใจสำคัญ**: แอปจะไปอ่าน `version.json` (จากลิงก์ Raw) เพื่อดูว่ามีเวอร์ชันใหม่ไหม ถ้ามี มันจะโหลด APK จาก `downloadUrl` ที่เราเขียนไว้ในไฟล์นั้นมาติดตั้งครับ
