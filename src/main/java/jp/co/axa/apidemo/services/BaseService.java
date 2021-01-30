package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.entities.VersionedEntity;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    @Setter
    ModelMapper mapper;

    public ResourceHeaderDTO getHeaderDTOFromEntity(VersionedEntity persitedObject) {

        return mapper.map(persitedObject,ResourceHeaderDTO.class);
    }
}
