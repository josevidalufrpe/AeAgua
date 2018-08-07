package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.inova.ufrpe.processos.carropipa.R;

public class AlertaCPFFragment extends DialogFragment {

    public String cpf;

    public interface CPFDialogListener {
        public void onDialogPositiveClick(DialogFragment dialogFragment);
        public void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_cpf, null);
        builder.setView(inflater.inflate(R.layout.dialog_cpf, null));
        builder.setTitle(R.string.informe_cpf)
                .setPositiveButton(R.string.btn_confirmar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText edt = view.findViewById(R.id.edt_cpf);
                        cpf = edt.getText().toString();
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
