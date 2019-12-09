package com.dataflow.components;

import com.model.Column;

import java.util.List;

public interface SourceInterface {
    List<Column> getOutputColumns();
}
