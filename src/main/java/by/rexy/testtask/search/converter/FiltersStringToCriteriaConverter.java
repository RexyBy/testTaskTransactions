package by.rexy.testtask.search.converter;

import by.rexy.testtask.search.SearchCriterion;

import java.util.List;

public interface FiltersStringToCriteriaConverter {
    List<SearchCriterion> convert(String filters);
}
