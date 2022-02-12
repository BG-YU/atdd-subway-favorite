package nextstep.station.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.common.domain.model.exception.EntityNotFoundException;
import nextstep.station.application.dto.StationRequest;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import nextstep.station.domain.repository.StationRepository;
import nextstep.common.domain.model.exception.FieldDuplicateException;

@RequiredArgsConstructor
@Service
@Transactional
public class StationService {
    private static final String ENTITY_NAME_FOR_EXCEPTION = "지하철 역";

    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public Station findById(Long id) {
        return stationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME_FOR_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public void verifyExists(long id) {
        if (!stationRepository.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NAME_FOR_EXCEPTION);
        }
    }

    public StationResponse saveStation(StationRequest request) {
        if (stationRepository.existsByName(request.getName())) {
            throw new FieldDuplicateException("역 이름");
        }
        Station station = stationRepository.save(
            new Station(request.getName())
        );

        return StationResponse.from(station);
    }

    @Transactional(readOnly = true)
    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }
}
