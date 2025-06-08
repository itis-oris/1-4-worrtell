<!DOCTYPE html>
<#import "spring.ftl" as spring/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход в систему</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">Вход</h3>
                </div>
                <div class="card-body">
                    <@spring.bind "loginForm"/>
                    <form action="/login" method="post" <#if spring.status.errors?exists>class="was-validated"</#if>>
                        <div class="mb-3">
                            <label for="email" class="form-label">Электронная почта</label>
                            <@spring.formInput "loginForm.email" "class=\"form-control\" id=\"email\" name=\"username\" required" "email"/>
                            <@spring.showErrors "<br>" "invalid-feedback"/>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Пароль</label>
                            <@spring.formInput "loginForm.password" "class=\"form-control\" id=\"password\" name=\"password\" required" "password"/>
                            <@spring.showErrors "<br>" "invalid-feedback"/>
                        </div>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <#if RequestParameters.error??>
                            <div class="alert alert-danger mb-3">Неверный email или пароль</div>
                        </#if>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">Войти</button>
                        </div>

                        <div class="mt-3 text-center">
                            <a href="/registration">Ещё нет аккаунта? Зарегистрируйтесь</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>