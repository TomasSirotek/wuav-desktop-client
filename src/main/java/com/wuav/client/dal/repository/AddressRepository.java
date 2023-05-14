package com.wuav.client.dal.repository;

import com.wuav.client.be.Address;
import com.wuav.client.dal.interfaces.IAddressRepository;
import com.wuav.client.dal.mappers.IAddressMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AddressRepository implements IAddressRepository {
    static Logger logger = LoggerFactory.getLogger(AddressRepository.class);

    @Override
    public int createAddress(AddressDTO addressDTO) {
        int affectedRowsResult = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IAddressMapper mapper = session.getMapper(IAddressMapper.class);
            var affectedRows = mapper.createAddress(
                    addressDTO.id(),
                    addressDTO.street(),
                    addressDTO.city(),
                    addressDTO.zipCode()
            );

            session.commit();
            affectedRowsResult = affectedRows > 0 ? 1 : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return affectedRowsResult;
    }


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

    @Override
    public boolean deleteAddressById(int id) {
        int affectedRows = 0;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IAddressMapper mapper = session.getMapper(IAddressMapper.class);
            affectedRows = mapper.deleteAddress(
                    id
            );
            session.commit();

            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }

        return false;
    }

}
