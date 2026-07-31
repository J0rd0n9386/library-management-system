package in.ankitboot.librarymanagment.repository;


import in.ankitboot.librarymanagment.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord,Long> {

    List<IssueRecord> findByMemberIdAndReturnedFalse(Long memberId);
    List<IssueRecord> findByBookCopyIdAndReturnedFalse(Long bookCopyId);
    List<IssueRecord> findByReturnedFalse();
}
