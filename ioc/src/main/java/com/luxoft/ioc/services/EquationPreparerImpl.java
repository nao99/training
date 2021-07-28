package com.luxoft.ioc.services;

import com.luxoft.ioc.model.DivisionEquation;
import com.luxoft.ioc.model.Equation;
import com.luxoft.ioc.model.MultiplicationEquation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquationPreparerImpl implements EquationPreparer {
    @Override
    public List<Equation> prepareEquationsFor(int base) {
        List<Equation> equations = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            var multiplicationEquation = new MultiplicationEquation(base, i);
            var divisionEquation = new DivisionEquation(multiplicationEquation.getResult(), base);
            equations.add(multiplicationEquation);
            equations.add(divisionEquation);

        }
        Collections.shuffle(equations);
        return equations;
    }
}
