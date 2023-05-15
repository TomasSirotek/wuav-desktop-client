package com.wuav.client.dal.repository;

import com.wuav.client.be.Address;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.dal.mappers.IAddressMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AddressRepositoryTest {

    private static final int EXISTING_ADDRESS_ID = 2055967059;

    private AddressRepository addressRepository;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private IAddressMapper iAddressMapper;


    @BeforeEach
    void setUp() {
        addressRepository = new AddressRepository();
        sqlSessionFactory = mock(SqlSessionFactory.class);
        sqlSession = mock(SqlSession.class);
        iAddressMapper = mock(IAddressMapper.class);
        doReturn(sqlSession).when(sqlSessionFactory).openSession();
        doReturn(iAddressMapper).when(sqlSession).getMapper(IAddressMapper.class);
        MyBatisConnectionFactory.setSqlSessionFactory(sqlSessionFactory);
    }

    @Test
    void testCreateAddress_Success() {
        int uniqueId = UniqueIdGenerator.generateUniqueId();
        AddressDTO addressDTO = new AddressDTO(uniqueId, "123 Street", "City", "12345");

        doReturn(1).when(iAddressMapper).createAddress(anyInt(), anyString(), anyString(), anyString());

        boolean result = addressRepository.createAddress(addressDTO);

        assertEquals(true, result);
        verify(sqlSession).commit();

        doReturn(addressDTO).when(iAddressMapper).getAddressById(uniqueId);
        Address retrievedAddress = addressRepository.getAddressById(uniqueId);
        assertEquals(addressDTO.id(), retrievedAddress.getId());
        assertEquals(addressDTO.street(), retrievedAddress.getStreet());
        assertEquals(addressDTO.city(), retrievedAddress.getCity());
        assertEquals(addressDTO.zipCode(), retrievedAddress.getZipCode());
    }


    @Test
    void getAddressById() {
        Address address = new Address(EXISTING_ADDRESS_ID, "123 Street", "City", "12345");

        doReturn(address).when(iAddressMapper).getAddressById(EXISTING_ADDRESS_ID);
        Address result = addressRepository.getAddressById(EXISTING_ADDRESS_ID);

        assertEquals(address, result);

    }

    @Test
    void updateAddress() {
        PutAddressDTO updatedAddress = new PutAddressDTO(EXISTING_ADDRESS_ID, "456 Avenue", "Town", "67890");

        doReturn(1).when(iAddressMapper).updateAddress(updatedAddress.id(), updatedAddress.street(), updatedAddress.city(), updatedAddress.zipCode());

        boolean result = addressRepository.updateAddress(updatedAddress);

        assertEquals(true, result);
        verify(sqlSession).commit();

        // Assuming that updateAddress method in repository also changes the stored address
        Address resultAddress = addressRepository.getAddressById(EXISTING_ADDRESS_ID);
        assertEquals(updatedAddress.id(), resultAddress.getId());
        assertEquals(updatedAddress.street(), resultAddress.getStreet());
        assertEquals(updatedAddress.city(), resultAddress.getCity());
        assertEquals(updatedAddress.zipCode(), resultAddress.getZipCode());
    }

}