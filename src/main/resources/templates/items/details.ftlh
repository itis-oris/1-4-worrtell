<!DOCTYPE html>
<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${item.name} - Детали товара</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .square-image-container {
            position: relative;
            width: 100%;
            max-width: 400px;
            margin-bottom: 20px;
            padding-bottom: 100%;
            overflow: hidden;
            cursor: pointer;
        }
        .square-image {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: opacity 0.3s ease;
            border-radius: 8px;
        }
        .thumbnail-container {
            display: flex;
            gap: 10px;
            margin-top: 10px;
            flex-wrap: wrap;
        }
    </style>
</head>
<body>
<#include "/fragments/navbar.ftlh">
<div class="container mt-4">
    <div class="row">
        <div class="col-md-5">
            <div class="square-image-container">
                <#if item.pictures?has_content>
                    <img class="square-image" id="mainImage"
                         src="${item.pictures[0]}"
                         alt="${item.name}">
                <#else>
                    <img class="square-image" src="/static/no-image.png" alt="Нет фото">
                </#if>
            </div>
            <div class="thumbnail-container">
                <#if item.pictures?has_content>
                    <#list item.pictures as pic>
                        <img class="thumbnail<#if pic == item.pictures[0]> active</#if>"
                             src="${pic}"
                             alt="thumbnail"
                             onclick="changeImage(this, '${pic}')">
                    </#list>
                </#if>
            </div>
        </div>

        <!-- Правая колонка: данные товара -->
        <div class="col-md-7">
            <h1>${item.name}</h1>
            <span class="badge bg-secondary mb-3">${item.category}</span>
            <div class="mb-3">
                <h3>${item.price} руб.</h3>
                <#if item.sold?has_content && item.quantity?has_content>
                    <span class="text-muted"><i class="bi bi-box"></i> Осталось: ${item.quantity?number - item.sold?number} шт.</span>
                </#if>
            </div>
            <div class="mb-3">
                <button class="btn btn-primary btn-lg py-3 fw-bold">Купить сейчас</button>
            </div>
            <div class="mb-3">
                <h5><i class="bi bi-info-circle"></i> Описание</h5>
                <p>${item.description!''}</p>
            </div>

            <div class="mb-3">
                <h5><i class="bi bi-truck"></i> Доставка</h5>
                <ul>
                    <li>
                        <span>По городу:</span>
                        <span>
                            <#if item.localDelivery == "true">${item.localDeliveryPrice} руб.<#else>Нет</#if>
                        </span>
                    </li>
                    <li>
                        <span>По стране:</span>
                        <span>
                            <#if item.delivery == "true">${item.countryDeliveryPrice} руб.<#else>Нет</#if>
                        </span>
                    </li>
                    <li>
                        <span>Международная:</span>
                        <span>
                            <#if item.delivery == "true">${item.worldDeliveryPrice} руб.<#else>Нет</#if>
                        </span>
                    </li>
                </ul>
            </div>

            <div class="mb-3">
                <h5><i class="bi bi-card-list"></i> Информация</h5>
                <ul>
                    <li><span>Состояние:</span> <span>${item.condition!''}</span></li>
                    <li><span>Город:</span> <span>${item.city!''}</span></li>
                    <li><span>Дата публикации:</span> <span>${item.listDateTime!''}</span></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script>
    function changeImage(thumbnail, newSrc) {
        document.getElementById('mainImage').src = newSrc;
        document.querySelectorAll('.thumbnail').forEach(function(item) {
            item.classList.remove('active');
        });
        thumbnail.classList.add('active');
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
