package com.example.api.Interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        //Si es una llamada a un controlador
        if(object instanceof HandlerMethod){
            Class<?> controllerClass = ((HandlerMethod) object).getBeanType();

            //Guarda las variables
            String controlador = controllerClass.getName();
            String metodo = ((HandlerMethod) object).getMethod().getName();
            String ipAddress = request.getRemoteAddr();
            LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String fechaFormateada = fechaActual.format(formatter);

            //Crea un registro de auditoria
            RegistroAuditoria reg = new RegistroAuditoria();
            reg.setControlador(controlador);
            reg.setMetodo(metodo);
            reg.setIpAddress(ipAddress);
            reg.setFecha(fechaFormateada);

            //Imprime el registro. Podría añadirse a un .txt o en una DB.
            System.out.println("Nuevo acceso: " +reg);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model){
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception){
    }
}