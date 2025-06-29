package dev.wortel.meshok.security.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@securityService.hasAccessToOrder(#orderId, principal.id, principal.authorities)")
public @interface CheckOrderAccess {
}