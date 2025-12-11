# 🚘 Mercedes-Benz Showroom Management App

[![Vue.js](https://img.shields.io/badge/Vue.js-3.5.17-42b883?style=for-the-badge&logo=vuedotjs&logoColor=white)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![TailwindCSS](https://img.shields.io/badge/TailwindCSS-4.1.12-38b2ac?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)
[![Build with ❤️](https://img.shields.io/badge/Built%20with-%E2%9D%A4-red?style=for-the-badge)]()

---

A **full-stack web application** for managing a **Mercedes-Benz showroom**, offering a **luxurious experience for users** and a **powerful admin interface for staff**.  

---

## ✨ Features

- 🧑‍💼 **Admin Dashboard** – Manage cars, users, and appointments with complete CRUD control.  
- 🚗 **User Dashboard** – Explore cars, book test drives, and view AI-generated descriptions.  
- 🤖 **AI Car Descriptions** – Generate elegant car overviews in English and French.  
- 🌓 **Dark Mode & i18n** – Switch between light/dark themes and languages seamlessly.  
- 🔐 **JWT Authentication** – Role-based security for Admin and User.  
- 📷 **360° Car Viewer** – Interactive car rotation and gallery mode.  

---

## 🖼️ Screenshots

🏠 **Home Page**  
![Home Page](frontend/mercedes/src/assets/userDashboard.png)

🚗 **Car**  
![Car Details](frontend/mercedes/src/assets/cars.png)

🧑‍💼 **Admin Dashboard**  
![Admin Dashboard](frontend/mercedes/src/assets/adminDashboard.png)

🤖 **AI Assistant**  
![AI Generator](frontend/mercedes/src/assets/ai.png)

🗓️ **Events**  
![Admin Dashboard](frontend/mercedes/src/assets/events.png)

---

## 🎥 Demo Video

🎬 *Experience the app in motion!*  
👉 [Watch the video](https://www.linkedin.com/) <!-- Replace with your real demo link -->

---

## 💼 Technical Details

- 👨‍💻 **Developer:** Yassin Medhioub  
- 🏢 **Project Type:** Summer Internship 
- 🧠 **Company Name:** Silver Star Sfax - Mercedes Benz Sfax   

---

### 🧩 Built With

#### 🖥️ Frontend
- Vue 3 (Composition API)  
- Vite  
- Tailwind CSS  
- Vue Router  
- Axios  
- LocalStorage  

#### 🧠 Backend
- Spring Boot  
- Spring Security (JWT)  
- Spring Data JPA  
- RESTful API  
- MySQL Database  
- Optional AI Service (OpenAI API Ready)  

---

## 🗂️ Database Structure

| Table | Description |
|--------|-------------|
| **cars** | id, model, type, year, price, images |
| **users** | id, username, role, status |
| **appointments** | id, user, car, date, status |
| **ai_descriptions** | id, carId, language, content |

---

## ⚙️ Installation

### 🖥️ Frontend
```bash
cd frontend
npm install
npm run dev
```

### 🤖 AI
```bash
cd frontend
npm run ai
```

### 🧠 Backend
```bash
cd backend
mvn spring-boot:run
```

> ⚙️ Configure your MySQL credentials and JWT secret inside `application.properties`.

---

## 📢 About the Project

> “Luxury isn’t only in the cars — it’s in every line of code.  
>  This app reflects the prestige, innovation, and elegance of Mercedes-Benz.”

---

## 👨‍🎓 Author

**Yassin Medhioub**  
Software Engineer Student | AI Engineering Enthusiast  
📧 medhioubyassin6@gmail.com 
🌐 [LinkedIn Profile]([https://github.com/NEMSS-DEV](https://www.linkedin.com/in/yassin-medhioub/))

---

> _“Luxury in every detail — from the dashboard to the codebase.”_
