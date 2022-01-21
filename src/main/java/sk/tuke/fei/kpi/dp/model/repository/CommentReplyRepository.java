package sk.tuke.fei.kpi.dp.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import sk.tuke.fei.kpi.dp.model.entity.CommentReply;

@Repository
public interface CommentReplyRepository extends CrudRepository<CommentReply, Long> {
}
