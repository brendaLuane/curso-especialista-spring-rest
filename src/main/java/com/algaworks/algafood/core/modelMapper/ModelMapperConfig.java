package com.algaworks.algafood.core.modelMapper;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();

//        modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//            .addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);

        modelMapper.createTypeMap(Endereco.class, EnderecoModel.class)
                .<String>addMapping(
                        src-> src.getCidade().getEstado().getNome(),
                        (dest, value)-> dest.getCidade().setEstado(value)
                );

        return modelMapper;
    }
}
