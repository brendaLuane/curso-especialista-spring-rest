package com.algaworks.algafood.infrascructure.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.algaworks.algafood.infrascructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrascructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired @Lazy

    RestauranteRepository restauranteRepository;

    public List<Restaurante> find(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal){

        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
        Root<Restaurante> root = criteria.from(Restaurante.class);

        ArrayList<Predicate> predicates = new ArrayList<Predicate>();

        if(StringUtils.hasText(nome))
            predicates.add(builder.like(root.get("nome"),"%"+ nome +"%"));

        if(taxaInicial != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaInicial));

        if(taxaFinal != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFinal));

        criteria.where(predicates.toArray(new Predicate[0]));

         TypedQuery<Restaurante> query = manager.createQuery(criteria);
         return query.getResultList();
    }

    @Override
    public List<Restaurante> findComFreteGratis(String nome) {
        return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
    }

    public List<Restaurante> findDinamico(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal){
        StringBuilder jpql = new StringBuilder();
        HashMap parametros = new HashMap<String, Object>();

        jpql.append("from Restaurante where 0 = 0 ");

        if(StringUtils.hasLength(nome)){
            jpql.append("and nome like :nome ");
            parametros.put("nome","%"+ nome +"%");
        }
        if(taxaInicial != null){
            jpql.append("and taxaFrete >= :taxaInicial ");
            parametros.put("taxaInicial", taxaInicial);
        }

        if(taxaFinal != null){
            jpql.append("and taxaFrete <= :taxaFinal ");
            parametros.put("taxaFinal", taxaFinal);
        }

        TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);

        parametros.forEach((chave, valor) -> query.setParameter(chave.toString(), valor));

        return query.getResultList();
    }
}
