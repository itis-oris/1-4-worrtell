<!DOCTYPE html>
<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Все товары</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
<nav class="navbar navbar-light bg-light mb-4">
    <div class="container-fluid">
        <a class="navbar-brand me-4" href="/">Магазин</a>

        <div class="flex-grow-1 mx-4">
            <form class="w-100" role="search" action="/items/search" method="get">
                <div class="input-group">
                    <input class="form-control"
                           type="search"
                           name="query"
                           placeholder="Поиск товаров"
                           aria-label="Search"
                           value="${searchQuery!''}">
                    <button class="btn btn-outline-success" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
                <input type="hidden" name="page" value="0">
                <input type="hidden" name="size" value="12">
            </form>

            <#if items?? && items?size == 0>
                <div class="alert alert-info text-center mt-3">
                    Товары не найдены.
                </div>
            </#if>
        </div>

        <div class="d-flex">
            <a class="btn btn-outline-secondary me-2" href="/cart">
                <i class="bi bi-cart"></i>
            </a>
            <a class="btn btn-outline-primary position-relative" href="/account">
                <i class="bi bi-person"></i>
                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                    0
                </span>
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <h1 class="mb-4 text-center">Все товары</h1>

    <#if items?size == 0>
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="alert alert-info text-center">
                    Товары не найдены.
                </div>
            </div>
        </div>
    <#else>
        <div class="row row-cols-1 row-cols-md-3 g-4">
            <#list items as item>
                <div class="col">
                    <div class="card h-100 shadow-sm">
                        <img src="https://meshok.net/i/${item.meshok_id?c}.0.jpg?1" class="card-img-top" alt="${item.name}" style="height: 200px; object-fit: cover;">
                        <div class="card-body">
                            <h5 class="card-title">${item.name}</h5>
                            <p class="card-text text-muted">
                                <small>${item.category}</small>
                            </p>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="price fw-bold">${item.price} руб.</span>
                                <a href="/items/${item.id}" class="btn btn-sm btn-outline-primary">Подробнее</a>
                            </div>
                        </div>
                        <div class="card-footer bg-transparent">
                            <small class="text-muted">
                                <i class="bi bi-geo-alt"></i> ${item.city!''}
                            </small>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>