package com.furyforever.resource;

import com.furyforever.dao.CarroDao;
import com.furyforever.dto.AtulizarCarroDTO;
import com.furyforever.dto.CadastrarCarroDTO;
import com.furyforever.dto.DetalhesCarroDTO;
import com.furyforever.exceptions.EntidadeNaoEncontradaException;
import com.furyforever.factory.ConnectionFactory;
import com.furyforever.model.Carro;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;

@Path("/carros")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarroResource {
    private CarroDao carroDao;
    private ModelMapper modelMapper;

    public CarroResource() throws SQLException {
        this.carroDao = new CarroDao(ConnectionFactory.getConnection());
        this.modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastrarCarroDTO dto, @Context UriInfo uriInfo) throws SQLException {
        Carro carro = modelMapper.map(dto, Carro.class);
        carroDao.cadastrar(carro);

        UriBuilder uri = uriInfo.getAbsolutePathBuilder();
        uri.path("carros/" + carro.getChassi());

        return Response.created(uri.build())
                .entity(modelMapper.map(carro, DetalhesCarroDTO.class)).build();
    }

    @GET
    @Path("{chassi}")
    public Carro buscar(@PathParam("chassi") String chassi) throws EntidadeNaoEncontradaException, SQLException {
        return carroDao.procurarPorChassi(chassi);
    }

    @GET
    public List<Carro> listar() throws SQLException {
        return carroDao.listar();
    }

    @PUT
    @Path("{chassi}")
    public Response atulizar(@Valid AtulizarCarroDTO dto, @PathParam("chassi") String chassi) throws EntidadeNaoEncontradaException, SQLException {
        Carro carro = modelMapper.map(dto, Carro.class);
        carro.setChassi(chassi);
        carroDao.atualizar(carro);

        return Response.ok().entity(modelMapper.map(carro, DetalhesCarroDTO.class)).build();
    }

    @DELETE
    @Path("{chassi}")
    public void remover(@PathParam("chassi") String chassi) throws EntidadeNaoEncontradaException, SQLException {
        carroDao.remover(chassi);
    }
}
