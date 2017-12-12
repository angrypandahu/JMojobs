package com.panda.mojobs.service.impl;

import com.panda.mojobs.domain.Experience;
import com.panda.mojobs.domain.Resume;
import com.panda.mojobs.domain.User;
import com.panda.mojobs.repository.ResumeRepository;
import com.panda.mojobs.repository.UserRepository;
import com.panda.mojobs.repository.search.ResumeSearchRepository;
import com.panda.mojobs.security.SecurityUtils;
import com.panda.mojobs.service.ResumeService;
import com.panda.mojobs.service.data.ResumeData;
import com.panda.mojobs.service.dto.BasicInformationDTO;
import com.panda.mojobs.service.dto.ExperienceDTO;
import com.panda.mojobs.service.dto.ResumeDTO;
import com.panda.mojobs.service.mapper.BasicInformationMapper;
import com.panda.mojobs.service.mapper.ExperienceMapper;
import com.panda.mojobs.service.mapper.ResumeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Resume.
 */
@Service
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);

    private final ResumeRepository resumeRepository;

    private final ResumeMapper resumeMapper;
    private final BasicInformationMapper basicInformationMapper;
    private final ExperienceMapper experienceMapper;

    private final ResumeSearchRepository resumeSearchRepository;

    private final UserRepository userRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository, ResumeMapper resumeMapper, BasicInformationMapper basicInformationMapper, ExperienceMapper experienceMapper, ResumeSearchRepository resumeSearchRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.resumeMapper = resumeMapper;
        this.basicInformationMapper = basicInformationMapper;
        this.experienceMapper = experienceMapper;
        this.resumeSearchRepository = resumeSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a resume.
     *
     * @param resumeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ResumeDTO save(ResumeDTO resumeDTO) {
        log.debug("Request to save Resume : {}", resumeDTO);
        Resume resume = resumeMapper.toEntity(resumeDTO);
        resume = resumeRepository.save(resume);
        ResumeDTO result = resumeMapper.toDto(resume);
        resumeSearchRepository.save(resume);
        return result;
    }

    /**
     * Get all the resumes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> findAll() {
        log.debug("Request to get all Resumes");
        return resumeRepository.findAll().stream()
            .map(resumeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one resume by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ResumeDTO findOne(Long id) {
        log.debug("Request to get Resume : {}", id);
        Resume resume = resumeRepository.findOne(id);
        return resumeMapper.toDto(resume);
    }

    /**
     * Delete the  resume by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resume : {}", id);
        resumeRepository.delete(id);
        resumeSearchRepository.delete(id);
    }

    /**
     * Search for the resume corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> search(String query) {
        log.debug("Request to search Resumes for query {}", query);
        return StreamSupport
            .stream(resumeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(resumeMapper::toDto)
            .collect(Collectors.toList());
    }


    @Override
    public List<ResumeDTO> findByCurrentUser() {
        List<Resume> resumeList = resumeRepository.findByUserIsCurrentUser();
        return resumeList.stream().map(resumeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ResumeData> findDataByCurrentUser() {
        List<Resume> resumeList = resumeRepository.findByUserIsCurrentUser();
        List<ResumeData> resumeDataList = new ArrayList<>();
        if (resumeList != null && resumeList.size() > 0) {
            return resumeList.stream().map(this::toResumeData).collect(Collectors.toList());
        }
        return resumeDataList;
    }

    @Override
    public ResumeData toResumeData(Resume resume) {
        ResumeData resumeData = new ResumeData();
        resumeData.setResumeDTO(resumeMapper.toDto(resume));
        BasicInformationDTO basicInformationDTO = basicInformationMapper.toDto(resume.getBasicInformation());
        resumeData.setBasicInformationDTO(basicInformationDTO == null ? new BasicInformationDTO() : basicInformationDTO);
        List<Experience> experienceList = new ArrayList<>();
        experienceList.addAll(resume.getExperiencies());
        List<ExperienceDTO> experienceDTOList = experienceMapper.toDto(experienceList);
        if (experienceDTOList == null) {
            experienceDTOList = new ArrayList<>();
        }
        resumeData.setExperienceDTOList(experienceDTOList);
        return resumeData;
    }

    @Override
    public ResumeData toResumeData(ResumeDTO resumeDTO) {
        Resume resume = resumeRepository.findOne(resumeDTO.getId());
        return toResumeData(resume);
    }

    @Override
    public ResumeDTO createByLoginUser() {
        Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (oneByLogin.isPresent()) {
            User user = oneByLogin.get();
            ResumeDTO resumeDTO = new ResumeDTO();
            resumeDTO.setUserId(user.getId());
            resumeDTO.setName(user.getLogin());
            return save(resumeDTO);
        }
        return null;
    }
}
