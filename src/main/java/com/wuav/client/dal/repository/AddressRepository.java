package com.wuav.client.dal.repository;

import com.wuav.client.be.Address;
import com.wuav.client.dal.interfaces.IAddressRepository;
import com.wuav.client.dal.mappers.IAddressMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AddressRepository class.
 */
public class AddressRepository implements IAddressRepository {
    private Logger logger = LoggerFactory.getLogger(AddressRepository.class);

    /**
     * Create a new address.
     *
     * @param addressDTO AddressDTO
     * @return boolean
     * @throws Exception Exception occurred while creating address
     */
    @Override
    public boolean createAddress(SqlSession session, AddressDTO addressDTO) throws Exception {
        try {
            IAddressMapper mapper = session.getMapper(IAddressMapper.class);
            int affectedRowsResult = mapper.createAddress(
                    addressDTO.id(),
                    addressDTO.street(),
                    addressDTO.city(),
                    addressDTO.zipCode()
            );
            return affectedRowsResult > 0;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Get an address by id.
     *
     * @param id int
     * @return Address
     */
    @Override
    public Address getAddressById(int id) {
        Address address = null;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IAddressMapper mapper = session.getMapper(IAddressMapper.class);
            address = mapper.getAddressById(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return address;
    }

    /**
     * Update an address.
     *
     * @param addressDTO PutAddressDTO
     * @return boolean
     */
    @Override
    public boolean updateAddress(PutAddressDTO addressDTO) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IAddressMapper mapper = session.getMapper(IAddressMapper.class);
            var affectedRows = mapper.updateAddress(
                    addressDTO.id(),
                    addressDTO.street(),
                    addressDTO.city(),
                    addressDTO.zipCode()
            );
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return false;
    }

    /**
     * Delete an address by id.
     *
     * @param id int
     * @return boolean
     */
    @Override
    public boolean deleteAddressById(SqlSession session, int id) {
        try {
            IAddressMapper mapper = session.getMapper(IAddressMapper.class);
            int affectedRows = mapper.deleteAddress(
                    id
            );
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            throw ex;
        }
    }

}
