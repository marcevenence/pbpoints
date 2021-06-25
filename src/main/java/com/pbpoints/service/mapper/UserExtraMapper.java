package com.pbpoints.service.mapper;

import com.pbpoints.domain.Authority;
import com.pbpoints.domain.UserExtra;
import com.pbpoints.service.dto.UserExtraDTO;
import com.pbpoints.web.rest.vm.ManagedUserVM;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, DocTypeMapper.class })
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {
    @Mapping(source = "docType", target = "docType")
    @Mapping(source = "user", target = "user")
    UserExtraDTO toDto(UserExtra userExtra);

    @Mapping(source = "docType", target = "docType")
    @Mapping(source = "user", target = "user")
    UserExtra toEntity(UserExtraDTO userExtraDTO);

    default UserExtra fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserExtra userExtra = new UserExtra();
        userExtra.setId(id);
        return userExtra;
    }

    @Mapping(source = "user.activated", target = "activated")
    @Mapping(source = "user.authorities", target = "authorities", qualifiedByName = "lalala")
    @Mapping(source = "user.createdBy", target = "createdBy")
    @Mapping(source = "user.createdDate", target = "createdDate")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.imageUrl", target = "imageUrl")
    @Mapping(source = "user.langKey", target = "langKey")
    @Mapping(source = "user.lastModifiedBy", target = "lastModifiedBy")
    @Mapping(source = "user.lastModifiedDate", target = "lastModifiedDate")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.id", target = "id")
    ManagedUserVM toManagedUserVM(UserExtra userExtra);

    @Named("lalala")
    default Set<String> authoritiesFromStrings(Set<Authority> authoritiesAsString) {
        Set<String> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                authoritiesAsString
                    .stream()
                    .map(
                        x -> {
                            return x.getName();
                        }
                    )
                    .collect(Collectors.toSet());
        }

        return authorities;
    }
}
