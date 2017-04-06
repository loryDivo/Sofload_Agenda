package com.soflod.agenda.swingui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.soflod.agenda.core.Attivita;
import com.soflod.agenda.core.Impegno;
import com.soflod.agenda.persistence.AttivitaORM;
import com.soflod.agenda.persistence.ImpegnoORM;
import com.soflod.agenda.persistence.exception.AttivitaNonValidaException;
import com.soflod.agenda.persistence.exception.ImpegnoOrmNonValidoException;

public class RenderCella extends DefaultListCellRenderer {

	private transient Logger logger = LoggerFactory.getLogger(RenderCella.class);
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
     
    	Color bgColor = null;
    	try {
			if (value instanceof AttivitaORM) {
				Attivita tmp = ((AttivitaORM) value).restituisciAttivita();
				bgColor = tmp.getCategoria().getColore();
			}

			if (value instanceof ImpegnoORM) {
				Impegno tmp = ((ImpegnoORM) value).restituisciImpegno();
				bgColor = tmp.getCategoria().getColore();
			}
			
    	} catch(ImpegnoOrmNonValidoException e) {
    		logger.info(e, e.getMessage());
    	} catch (AttivitaNonValidaException e) {
    		logger.info(e, e.getMessage());
		}
    	Component renderComponent = super.getListCellRendererComponent(
    			list, value, index, isSelected, cellHasFocus);
    	
    	if (bgColor != null) {
    		renderComponent.setBackground(bgColor);
    	}
        return renderComponent;
    }
}
