package com.furyforever.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.sql.SQLException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();

        if (e instanceof EntidadeNaoEncontradaException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Recurso não encontrado: " + e.getMessage())
                    .build();
        } else if (e instanceof SQLException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro de banco de dados: " + e.getMessage())
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Erro interno do servidor: " + e.getMessage())
                .build();
    }
}
