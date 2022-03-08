package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;
import sk.tuke.fei.kpi.dp.model.entity.Image;

@Mapper(componentModel = "jsr330", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Singleton
public interface ImageMapper {

  ImageInfoDto imageToImageInfoDto(Image image);

}
