package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.Mapper;
import sk.tuke.fei.kpi.dp.dto.ImageInfoDto;
import sk.tuke.fei.kpi.dp.model.entity.Image;

@Mapper(componentModel = "jsr330")
@Singleton
public interface ImageMapper {

  ImageInfoDto imageToImageInfoDto(Image image);

}
