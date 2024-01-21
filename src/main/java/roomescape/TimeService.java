package roomescape;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Time> findAllTime() {
        TimeQueryingDAO timeQueryingDAO = TimeQueryingDAO.getInstance(jdbcTemplate);
        return timeQueryingDAO.findAllTimes();
    }

    public Long createTime(Time time) {
        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);
        return timeUpdatingDAO.insertWithKeyHolder(time);
    }

    public boolean deleteTime(Long id) {
        TimeUpdatingDAO timeUpdatingDAO = TimeUpdatingDAO.getInstance(jdbcTemplate);
        return timeUpdatingDAO.delete(id);
    }


}
