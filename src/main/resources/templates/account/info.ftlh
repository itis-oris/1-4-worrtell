<!DOCTYPE html>
<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль - Аккаунт</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
<#include "/fragments/navbar.ftlh">
<div class="container">
    <h1 class="mb-3"><i class="bi bi-person-circle"></i> Личный кабинет</h1>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Информация о профиле</h5>
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><strong>Имя пользователя:</strong> ${user.email}</li>
                <#-- Добавьте другие поля профиля по необходимости -->
                <#-- <li class="list-group-item"><strong>Email:</strong> ${user.email!}</li> -->
            </ul>
            <hr>
            <a href="/logout" class="btn btn-danger mt-2"><i class="bi bi-box-arrow-right"></i> Выйти</a>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
