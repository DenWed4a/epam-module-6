package com.epam.esm.pagination;

/**
 * Class Pagination
 * @author Dzianis Savastsiuk
 */
public class Pagination {
    /**
     * Gets first result of list objects
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return int
     */
    public static int firstResult(int pageNumber, int pageSize){
        return pageSize * (pageNumber - 1);
    }

    /**
     * Gets last page of list objects
     * @param numberOfEntries the number of entries
     * @param pageSize the page size
     * @return int
     */
    public static int lastPage(int numberOfEntries, int pageSize){
        return numberOfEntries/pageSize +1;
    }




}
