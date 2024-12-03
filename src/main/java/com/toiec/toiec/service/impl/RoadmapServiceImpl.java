package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.roodmap.CreateDay;
import com.toiec.toiec.dto.request.roodmap.CreateGrammar;
import com.toiec.toiec.dto.request.roodmap.UpdateDay;
import com.toiec.toiec.dto.request.roodmap.UpdateGrammar;
import com.toiec.toiec.dto.response.roadmap.DayResponse;
import com.toiec.toiec.dto.response.roadmap.GrammarResponse;
import com.toiec.toiec.dto.response.roadmap.RoadmapResponse;
import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.entity.LessonDetail;
import com.toiec.toiec.exception.base.NotFoundException;
import com.toiec.toiec.repository.GrammarRepository;
import com.toiec.toiec.repository.RoadmapRepository;
import com.toiec.toiec.service.RoadmapService;
import com.toiec.toiec.utils.JsonUtils;
import com.toiec.toiec.utils.MapperUtils;
import com.toiec.toiec.utils.UpdateUtils;
import com.toiec.toiec.utils.WordToHtmlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final GrammarRepository grammarRepository;

    @Override
    public DayResponse createdDay(CreateDay createDay)
    {
        Lesson dayLesson = new Lesson();
        dayLesson.setNameLesson(createDay.getNameDay());
        dayLesson.setType("roadmap");
        roadmapRepository.save(dayLesson);
        return new DayResponse(dayLesson);
    }
    @Override
    public DayResponse updateDay(UpdateDay updateDay, Integer dayId)
    {
        Lesson dayLesson = roadmapRepository.findById(dayId).orElseThrow(()->new NotFoundException());
        dayLesson.setNameLesson(updateDay.getNameDay());
        roadmapRepository.save(dayLesson);
        return new DayResponse(dayLesson);
    }
    @Override
    public void deleteDay(Integer dayId)
    {
        Lesson dayLesson = roadmapRepository.findById(dayId).orElseThrow(()->new NotFoundException());
        roadmapRepository.delete(dayLesson);
    }
    @Override
    public List<GrammarResponse> findLessonesByNameLesson(String nameLesson, Pageable pageable)
    {
        Page<LessonDetail> lessonDetailList = grammarRepository.findByNameLessonContainingIgnoreCaseAndType(nameLesson,"roadmap", pageable);
        return MapperUtils.toDTOs(lessonDetailList.toList(),GrammarResponse.class);
    }

    @Override
    public RoadmapResponse updateLessonForDay(List<Integer> lsLessonDetailIdPost,Integer dayId) throws IOException {
        Lesson day = roadmapRepository.findById(dayId).orElseThrow(()->new NotFoundException());
        RoadmapResponse roadmapResponse = new RoadmapResponse();
        roadmapResponse.setId(dayId);
        roadmapResponse.setName(day.getNameLesson());
        List<LessonDetail> lsLessonDetail= grammarRepository.findByLesson_IdLesson(dayId);
        Set<Integer> setLessonDetailId = new HashSet<>(lsLessonDetailIdPost);

        Map<Integer,LessonDetail> grammarResponseMap = new HashMap<>();
        List<LessonDetail> lessonDetailSave = new ArrayList<>();
        for(LessonDetail lessonDetail : lsLessonDetail){
            if(!setLessonDetailId.contains(lessonDetail.getIdLessonDetail()))
            {
                lessonDetail.setLesson(null);
                lessonDetailSave.add(lessonDetail);
            }
            grammarResponseMap.put(lessonDetail.getIdLessonDetail(),lessonDetail);
        }
        for(Integer lessonDetailId: setLessonDetailId){
            if(!grammarResponseMap.containsKey(lessonDetailId))
            {
                LessonDetail lessonDetail = grammarRepository.findById(lessonDetailId)
                        .orElseThrow(() -> new NotFoundException());
                lessonDetail.setIdLessonDetail(lessonDetailId);
                lessonDetail.setLesson(day);
                lessonDetailSave.add(lessonDetail);
            }
        }
        lsLessonDetail=grammarRepository.saveAll(lessonDetailSave);
        List<GrammarResponse> grammarResponseList = MapperUtils.toDTOs(lsLessonDetail,GrammarResponse.class);
        roadmapResponse.setGrammars(grammarResponseList);
        return roadmapResponse;
    }

    @Override
    public List<RoadmapResponse> findAllRoadMap() throws IOException {
        List<RoadmapResponse> responseListResponse = new ArrayList<>();
        List<Object[]> objects = roadmapRepository.findAllRoadmapWithLessonDetail();
        for(Object[] object: objects){
            RoadmapResponse roadmapResponse = new RoadmapResponse();
            roadmapResponse.setId((Integer) object[0]);
            roadmapResponse.setName((String)object[1]);
            if(object[2]!=null){
                List<GrammarResponse> grammarResponseList= JsonUtils.fromJsonList((String)object[2], GrammarResponse.class);
                if(grammarResponseList.get(0).getIdDetail()!=null)roadmapResponse.setGrammars(grammarResponseList);
                else roadmapResponse.setGrammars(new ArrayList<>());
            }
            responseListResponse.add(roadmapResponse);
        }
        return responseListResponse;
    }
    @Override
    public  RoadmapResponse findLessonesByRoodmapId(Integer roadmapId) throws IOException {
        RoadmapResponse roadmapResponse = new RoadmapResponse();
        List<Object[]> objects = roadmapRepository.findRoadmapWithLessonDetail(roadmapId);
        if(objects.size()==0) throw new NotFoundException();
        roadmapResponse.setId(roadmapId);
        roadmapResponse.setName((String)objects.get(0)[1]);
        if(objects.get(0)[2]!=null){
            List<GrammarResponse> grammarResponseList= JsonUtils.fromJsonList((String)objects.get(0)[2], GrammarResponse.class);
            roadmapResponse.setGrammars(grammarResponseList);
        }
        return roadmapResponse;
    }

    @Override
    public GrammarResponse getGrammarById(Integer grammarId)
    {
        LessonDetail lessonDetail=grammarRepository.findById(grammarId).orElseThrow(NotFoundException::new);
        GrammarResponse grammarRepository = new GrammarResponse();
        grammarRepository.setContent(lessonDetail.getContent());
        grammarRepository.setType(lessonDetail.getType());
        grammarRepository.setNameLesson(lessonDetail.getNameLesson());
        grammarRepository.setIdDetail(grammarId);
        return grammarRepository;
    }

    @Override
    public List<GrammarResponse> findAllLessonDetail( Pageable pageable) throws IOException {
        Page<LessonDetail> lessonDetailList = grammarRepository.findByType("roadmap", pageable);
        return MapperUtils.toDTOs(lessonDetailList.toList(),GrammarResponse.class);
    }

    @Override
    public GrammarResponse createGrammar(CreateGrammar createGrammar, MultipartFile word){
        LessonDetail lessonDetail = new LessonDetail();
        lessonDetail.setNameLesson(createGrammar.getNameLesson());
        lessonDetail.setType("roadmap");
        String htmlContent = WordToHtmlUtils.covertToHtml(word);
        lessonDetail.setContent(htmlContent);
        lessonDetail= grammarRepository.save(lessonDetail);
        return MapperUtils.toDTO(lessonDetail,GrammarResponse.class);
    }

    @Override
    public GrammarResponse updateGrammar(Integer idGrammar, UpdateGrammar updateGrammar, MultipartFile file)
    {
        LessonDetail grammar=grammarRepository.findById(idGrammar).orElseThrow(NotFoundException::new);
        if(file!=null)
        {
            String htmlContent = WordToHtmlUtils.covertToHtml(file);
            grammar.setContent(htmlContent);
        }
        if(updateGrammar!=null)
        UpdateUtils.updateEntityFromDTO(grammar,updateGrammar);
        grammar=grammarRepository.save(grammar);
        return MapperUtils.toDTO(grammar,GrammarResponse.class);

    }
}
