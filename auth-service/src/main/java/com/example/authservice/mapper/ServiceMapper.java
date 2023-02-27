package com.example.authservice.mapper;


import com.example.authservice.dto.appservice.ServiceCustomDto;
import com.example.authservice.dto.service.CustomServiceDto;
import com.example.authservice.dto.service.ServiceRequestDto;
import com.example.authservice.dto.service.ServiceResponseDto;
import com.example.authservice.entities.service.Service;
import com.example.authservice.entities.service.ServiceTag;
import com.example.authservice.entities.service.Tag;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SystemMapper.class, ExternalApiMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ServiceMapper {

  public abstract ServiceCustomDto toDto(Service api);

  public abstract CustomServiceDto toCustomDto(Service api);

  public abstract Service fromDto(ServiceCustomDto apiDto);


  public abstract Service fromServiceRequestDto(ServiceRequestDto serviceRequestDto);

  @Mapping(target = "nameSystem", source = "system.name")
  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ServiceResponseDto toServiceResponseDto(Service service);

//  @AfterMapping
//  public void afterMapping(@MappingTarget ServiceResponseDto serviceResponseDto, Service service) {
//    Set<String> tags = new HashSet<>();
//    List<ServiceTag> serviceTags = service.getServiceTags();
//    if (serviceTags != null) {
//      List<ServiceTag> listServiceTagFilter = serviceTags.stream()
//          .filter(serviceTag -> (serviceTag.getIsDeleted() != null || !serviceTag.getIsDeleted())).collect(Collectors.toList());
//
//      listServiceTagFilter.forEach(serviceTag -> {
//        Tag tag = serviceTag.getTag();
//        if (tag != null) {
//          tags.add(tag.getTag());
//        }
//      });
//    }
//    serviceResponseDto.setTags(tags);
//  }


  @Mapping(target = "code", source = "code", ignore = true)
  public abstract void updateModel(@MappingTarget Service service,
      ServiceRequestDto serviceRequestDto);

}
