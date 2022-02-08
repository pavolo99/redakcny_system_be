package sk.tuke.fei.kpi.dp.mapper;

import java.util.List;
import javax.inject.Singleton;
import org.mapstruct.Mapper;
import sk.tuke.fei.kpi.dp.dto.CommentDto;
import sk.tuke.fei.kpi.dp.dto.CommentReplyDto;
import sk.tuke.fei.kpi.dp.dto.create.CommentCreateDto;
import sk.tuke.fei.kpi.dp.model.entity.Comment;
import sk.tuke.fei.kpi.dp.model.entity.CommentReply;

@Mapper(componentModel = "jsr330")
@Singleton
public interface CommentMapper {

  CommentDto commentToCommentDto(Comment comment);

  List<CommentReplyDto> commentRepliesToCommentRepliesDtoList(List<CommentReply> commentReplies);

  Comment commentCreateDtoToComment(CommentCreateDto commentCreateDto);

}
