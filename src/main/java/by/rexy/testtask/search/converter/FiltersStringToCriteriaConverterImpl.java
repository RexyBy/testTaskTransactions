package by.rexy.testtask.search.converter;

import by.rexy.testtask.search.SearchCriterion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FiltersStringToCriteriaConverterImpl implements FiltersStringToCriteriaConverter {
    @Override
    public List<SearchCriterion> convert(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return Collections.emptyList();
        }

        Pattern pattern = Pattern.compile("(\\w+)(?::)(.*?)(?:,|$)");
        Matcher matcher = pattern.matcher(searchString);

        List<SearchCriterion> searchCriteria = new ArrayList<>();

        while (matcher.find()) {
            searchCriteria.add(new SearchCriterion(matcher.group(1), matcher.group(2)));
        }

        return searchCriteria;
    }
}
