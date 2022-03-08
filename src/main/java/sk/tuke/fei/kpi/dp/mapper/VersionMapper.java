package sk.tuke.fei.kpi.dp.mapper;

import javax.inject.Singleton;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sk.tuke.fei.kpi.dp.dto.VersionDto;
import sk.tuke.fei.kpi.dp.dto.simple.VersionSimpleDto;
import sk.tuke.fei.kpi.dp.model.entity.Version;

@Mapper(componentModel = "jsr330", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Singleton
public interface VersionMapper {

  VersionSimpleDto versionToVersionSimpleDto(Version version);

  VersionDto versionToVersionDto(Version version);
}
