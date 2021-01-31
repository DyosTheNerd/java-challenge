package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.entities.VersionedEntity;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is the base class for services providing useful implementations across services.
 */
public class BaseService {

    @Autowired
    @Setter
    ModelMapper mapper;

    /**
     * Maps a Persisted Domain Object onto a ResourceHeaderDTO, the standard information object to share
     * ID and versioning information only
     *
     * @param persitedObject
     * @return the header representing crucial information.
     */
    public ResourceHeaderDTO getHeaderDTOFromEntity(VersionedEntity persitedObject) {

        return mapper.map(persitedObject,ResourceHeaderDTO.class);
    }
}
