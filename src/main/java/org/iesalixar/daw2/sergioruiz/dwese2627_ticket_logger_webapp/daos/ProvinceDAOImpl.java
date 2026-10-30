package org.iesalixar.daw2.sergioruiz.dwese2627_ticket_logger_webapp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.sergioruiz.dwese2627_ticket_logger_webapp.entities.Province;
import org.iesalixar.daw2.sergioruiz.dwese2627_ticket_logger_webapp.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;


@Repository
@Transactional
public class ProvinceDAOImpl implements ProvinceDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Province> listAllProvinces() {
        logger.info("Listing all provinces with their regions from the database.");
        String sql = "SELECT p FROM Province p JOIN FETCH p.region";
        List<Province> provinces=entityManager.createQuery(sql, Province.class).getResultList();
        logger.info("Retrieved {} provinces from the database.", provinces.size());
        return provinces;
    }

    /**
     * Verifica si existe una provincia con el código especificado.
     * @param code código de la provincia a verificar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existsProvinceByCode(String code) {
        logger.info("Checking if province with code: {} exists", code);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?";
        Long count = entityManager.createQuery(sql,Long.class)
                .setParameter("code",code.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists: {}", code, exists);
        return exists;
    }

    /**
     * Verifica si existe una provincia con el código especificado,
     * excluyendo una provincia con un ID específico.
     * @param code código de la provincia a verificar.
     * @param id   ID de la provincia a excluir.
     * @return true si existe otra provincia con ese código, false en caso contrario.
     */
    @Override
    public boolean existsProvinceByCodeAndNotId(String code, Long id) {
        logger.info("Checking if province with code: {} exists excluding id: {}", code, id);
        String sql = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code AND id != :id";
        Long count = entityManager.createQuery(sql,Long.class)
                .setParameter("code",code.toUpperCase())
                .setParameter("id",id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Provincia a insertar (debe incluir región con id).
     */
    @Override
    public void insertProvince(Province province) {
        logger.info("Inserting province with code: {}, name: {}, regionId: {}",
                province.getCode(),
                province.getName(),
                province.getRegion() != null ? province.getRegion().getId() : null
        );

        entityManager.persist(province);
        logger.info("Inserted province succesful");
    }
    @Override
    public Province getProvinceById(Long id) {
        logger.info("Retrieving province by id: {}", id);
        Province province = entityManager.find(Province.class,id);
        if(province != null){
            logger.info("Province retrieved:{}-{}",province.getId(),province.getName());
        } else {
            logger.warn("Province wit id {} not found",province.getId());
        }
        return  province;
    }
    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Provincia a actualizar (debe incluir id y región con id).
     */
    @Override
    public void updateProvince(Province province) {
        logger.info("Updating province with id: {}", province.getId());
        entityManager.merge(province);
        logger.info("Updated province succesfil");
    }
    /**
     * Elimina una provincia de la base de datos.
     * @param id ID de la provincia a eliminar
     */
    @Override
    public void deleteProvince(Long id) {
        logger.info("Deleting province with id: {}", id);
        Province province = entityManager.find(Province.class,id);
        if(province!=null){
            entityManager.remove(province);
            logger.info("Province removed succesful");
        }else {
            logger.warn("Province with id {} doesn't exists",province.getId());
        }
    }
}