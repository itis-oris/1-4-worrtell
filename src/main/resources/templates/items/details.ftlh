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
        }
        .thumbnail-container {
            display: flex;
            gap: 10px;
            margin-top: 15px;
            flex-wrap: wrap;
        }
        .thumbnail {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border: 2px solid transparent;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        .thumbnail:hover, .thumbnail.active {
            border-color: #0d6efd;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm">
                <div class="square-image-container">
                    <img id="mainImage" src="https://meshok.net/i/${item.meshok_id?c}.0.jpg?1" class="square-image" alt="${item.name}">
                </div>
                <div class="thumbnail-container">
                    <img src="https://meshok.net/i/${item.meshok_id?c}.0.jpg?1" class="thumbnail active"
                         onclick="changeImage(this, 'https://meshok.net/i/${item.meshok_id?c}.0.jpg?1')">

                    <#list 1..9 as i>
                        <img src="https://meshok.net/i/${item.meshok_id?c}.${i}.jpg?1" class="thumbnail"
                             onclick="changeImage(this, 'https://meshok.net/i/${item.meshok_id?c}.${i}.jpg?1')"
                             onerror="this.style.display='none'">
                    </#list>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="d-flex justify-content-between align-items-start mb-3">
                <h1 class="mb-0">${item.name}</h1>
                <span class="badge bg-secondary">${item.category}</span>
            </div>

            <div class="d-flex align-items-center mb-4">
                <h3 class="price fw-bold mb-0 me-3">${item.price} руб.</h3>
                <#if item.sold?has_content && item.quantity?has_content>
                    <span class="text-muted"><i class="bi bi-box-seam"></i> Осталось: ${item.quantity?number - item.sold?number} шт.</span>
                </#if>
            </div>

            <div class="d-grid gap-2 mb-4">
                <button class="btn btn-primary btn-lg py-3 fw-bold">Купить сейчас</button>
            </div>

            <div class="card mb-4 shadow-sm">
                <div class="card-header bg-white">
                    <h5 class="mb-0"><i class="bi bi-card-text"></i> Описание</h5>
                </div>
                <div class="card-body">
                    <p class="card-text">${item.description!''}</p>
                </div>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-md-6">
                    <div class="card h-100 shadow-sm">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="bi bi-truck"></i> Доставка</h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-unstyled">
                                <li class="mb-2 d-flex justify-content-between">
                                    <span>По городу:</span>
                                    <span class="fw-bold <#if item.localDelivery == "true">text-success<#else>text-danger</#if>">
                                        <#if item.localDelivery == "true">${item.localDeliveryPrice} руб.<#else>Нет</#if>
                                    </span>
                                </li>
                                <li class="mb-2 d-flex justify-content-between">
                                    <span>По стране:</span>
                                    <span class="fw-bold <#if item.delivery == "true">text-success<#else>text-danger</#if>">
                                        <#if item.delivery == "true">${item.countryDeliveryPrice} руб.<#else>Нет</#if>
                                    </span>
                                </li>
                                <li class="d-flex justify-content-between">
                                    <span>Международная:</span>
                                    <span class="fw-bold <#if item.delivery == "true">text-success<#else>text-danger</#if>">
                                        <#if item.delivery == "true">${item.worldDeliveryPrice} руб.<#else>Нет</#if>
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card h-100 shadow-sm">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="bi bi-info-circle"></i> Информация</h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-unstyled">
                                <li class="mb-2 d-flex justify-content-between">
                                    <span>Состояние:</span>
                                    <span class="fw-bold">${item.condition!''}</span>
                                </li>
                                <li class="mb-2 d-flex justify-content-between">
                                    <span>Город:</span>
                                    <span class="fw-bold">${item.city!''}</span>
                                </li>
                                <li class="d-flex justify-content-between">
                                    <span>Дата публикации:</span>
                                    <span class="fw-bold">${item.listDateTime!''}</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
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