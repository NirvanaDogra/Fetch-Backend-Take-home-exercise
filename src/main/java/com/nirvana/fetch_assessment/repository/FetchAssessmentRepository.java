package com.nirvana.fetch_assessment.repository;

import com.nirvana.fetch_assessment.dto.ReceiptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchAssessmentRepository extends JpaRepository<ReceiptInfo, String> {
}
