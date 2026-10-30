package org.iesalixar.daw2.sergioruiz.dwese2627_ticket_logger_webapp.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.sergioruiz.dwese2627_ticket_logger_webapp.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class RegionDAOImpl implements RegionDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lista todas las regiones de la base de datos.
     * @return Lista de regiones
     */
    @Override
    public List<Region> listAllRegions() {
        logger.info("Listing all regions from the database.");
        String sql = "SELECT r FROM Region";
        List<Region> regions = entityManager.createQuery(sql, Region.class).getResultList();
        logger.info("Retrieved {} regions from the database.", regions.size());
        return regions;
    }

    /**
     * Inserta una nueva región en la base de datos.
     * @param region Región a insertar
     */
    @Override
    public void insertRegion(Region region) {
        logger.info("Inserting region with code: {} and name: {}", region.getCode(), region.getName());
        entityManager.persist(region);
        logger.info("insert region succesful");
    }

    /**
     * Actualiza una región existente en la base de datos.
     * @param region Región a actualizar
     */
    @Override
    public void updateRegion(Region region) {
        logger.info("Updating region with id: {}", region.getId());
        entityManager.merge(region);
        logger.info("Updated region with id {}",region.getId());
    }

    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     */
    @Override
    public void deleteRegion(Long id) {
        logger.info("Deleting region with id: {}", id);
        Region region = entityManager.find(Region.class,id);
        if(region!=null){
            entityManager.remove(region);
            logger.info("Region with id {} removed",region.getId());
        }else {
            logger.warn("Region with id {} not found",region.getId());
        }
    }

    /**
     * Recupera una región por su ID.
     * @param id ID de la región a recuperar
     * @return Región encontrada o null si no existe
     */
    @Override
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        Region region = entityManager.find(Region.class,id);
        if(region!=null){
            logger.info("Region find with code {} and id {}",region.getCode(),region.getId());
        }else {
            logger.warn("Region with id {} not found",region.getId());
        }
        return region;
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos.
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     */
    @Override
    public boolean existsRegionByCode(String code) {
        logger.info("Checking if region with code: {} exists", code);
        String sql = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code";
        Long count = entityManager.createQuery(sql, Long.class)
                .setParameter("code",code.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists: {}", code, exists);
        return exists;
    }
    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * excluyendo una región con un ID específico.
     * @param code el código de la región a verificar.
     * @param id   el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id) {
        logger.info("Checking if region with code: {} exists excluding id: {}", code, id);
        String sql = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code AND id != :id";
        Long count = entityManager.createQuery(sql, Long.class)
                .setParameter("code",code.toUpperCase())
                .setParameter("id",id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }
}