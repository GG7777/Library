package net.onlinelibrary.filter;

import java.util.List;

public interface Filter<TFiltered> {
    TFiltered doFilter(TFiltered obj);

    List<TFiltered> doFilter(List<TFiltered> objects);
}
