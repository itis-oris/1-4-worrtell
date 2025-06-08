<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meshok Market</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card-img-top {
            height: 200px;
            object-fit: contain;
        }
        .card {
            height: 100%;
            transition: transform 0.2s;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .navbar {
            margin-bottom: 20px;
        }
        .price {
            font-weight: bold;
            color: #d63384;
            font-size: 1.2rem;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/items">Meshok Market</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/items">Все товары</a>
                </li>
                <#-- Пример категории -->
                <li class="nav-item">
                    <a class="nav-link" href="/items/category/Электроника">Электроника</a>
                </li>
            </ul>
            <form class="d-flex" action="/items/search" method="get">
                <input class="form-control me-2" type="search" name="query" placeholder="Поиск...">
                <button class="btn btn-outline-light" type="submit">Найти</button>
            </form>
        </div>
    </div>
</nav>

<footer class="bg-dark text-white mt-5 py-3">
    <div class="container text-center">
        &copy; 2023 Meshok Market. Все права защищены.
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>