package com.luxoft.ioc.services;

import com.luxoft.ioc.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
