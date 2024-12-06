package org.eam.code.vmixapp.util;

import org.eam.code.vmixapp.model.Sequence;

public class SelectedSequence {
    private static Sequence selectedSequence;

    public static Sequence getSelectedSequence() {
        return selectedSequence;
    }

    public static void setSelectedSequence(Sequence selectedSequence) {
        SelectedSequence.selectedSequence = selectedSequence;
    }
}
