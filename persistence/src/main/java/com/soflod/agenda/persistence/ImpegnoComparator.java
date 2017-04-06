package com.soflod.agenda.persistence;

import java.util.Comparator;

import com.soflod.agenda.core.Impegno;

public class ImpegnoComparator implements Comparator<Impegno> {

	public ImpegnoComparator() {
		super();
	}

	public int compare(Impegno o1, Impegno o2) {
		return o1.getDataFine().compareTo(o2.getDataFine());
	}
}
