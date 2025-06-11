<nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Магазин</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain"
                aria-controls="navbarMain" aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="/items">Все товары</a></li>
            </ul>
            <form class="d-flex me-4" role="search" action="/items/search" method="get">
                <input class="form-control me-2" type="search" name="query"
                       placeholder="Поиск товаров" aria-label="Search" value="${searchQuery!''}">
                <button class="btn btn-outline-success" type="submit">
                    <i class="bi bi-search"></i>
                </button>
                <input type="hidden" name="page" value="0">
                <input type="hidden" name="size" value="12">
            </form>
            <ul class="navbar-nav mb-2 mb-lg-0">
                <li class="nav-item me-3">
                    <a class="nav-link position-relative" href="/cart">
                        <i class="bi bi-cart"></i> Корзина
                        <#-- Если хочешь — можно добавить количество товаров в корзине!
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                            ${cartCount!0}
                        </span>
                        -->
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/account">
                        <i class="bi bi-person"></i> Личный кабинет
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
