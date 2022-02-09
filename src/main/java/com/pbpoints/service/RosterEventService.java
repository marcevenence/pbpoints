package com.pbpoints.service;

import com.pbpoints.domain.RosterEvent;
import com.pbpoints.repository.RosterEventRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RosterEventService {

    private final Logger log = LoggerFactory.getLogger(RosterEventService.class);

    private final RosterEventRepository rosterEventRepository;

    public RosterEventService(RosterEventRepository rosterEventRepository) {
        this.rosterEventRepository = rosterEventRepository;
    }

    @Transactional(readOnly = true)
    public List<RosterEvent> findByEvCatId(Long evCatId) {
        log.debug("Request to get all Rosters");
        return rosterEventRepository.findByEvCatId(evCatId);
    }
}
