package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ia.dna.BoolArray;
import ia.dna.NeuralNet;
import view.menu.DialogChooseAIType;

public class TypeOfAI implements ActionListener {
	DialogChooseAIType dialog;
	public TypeOfAI(DialogChooseAIType dialog) {
		this.dialog = dialog;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(dialog.getMeshRadio().isSelected()) {
			dialog.setDnaUsed(BoolArray.class);
			dialog.setFramesPerAction(1);
		}else if(dialog.getNNRadio().isSelected()) {
			dialog.setDnaUsed(NeuralNet.class);
			dialog.setFramesPerAction(2);

		}
		
		dialog.setSizePop(dialog.getSizePop());
		this.dialog.dispose();
	}

}
