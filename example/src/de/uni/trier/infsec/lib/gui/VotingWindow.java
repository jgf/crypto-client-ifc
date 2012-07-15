package de.uni.trier.infsec.lib.gui;

import java.net.URL;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.ListViewSelectionListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Span;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import de.uni.trier.infsec.environment.network.NetworkError;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Decryptor;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Encryptor;
import de.uni.trier.infsec.protocols.simplevoting.Voter;
import de.uni.trier.infsec.protocols.simplevoting.VotingProtocol.Votes;

public class VotingWindow extends Window implements Bindable,
		ButtonPressListener, ListViewSelectionListener {

	PushButton button = null;
	ListView list = null;
	Label vote = null;
	TextInput pubKey = null;
	TextInput privKey = null;
	TextInput credent = null;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {
		button = (PushButton) namespace.get("pushButton");
		button.getButtonPressListeners().add(this);

		vote = (Label) namespace.get("currentVote");

		pubKey = (TextInput) namespace.get("publickey");
		privKey = (TextInput) namespace.get("privatekey");
		credent = (TextInput) namespace.get("credential");

		list = (ListView) namespace.get("voteList");
		list.getListViewSelectionListeners().add(this);
		list.setListData(new ArrayList<Votes>(Votes.values()));
	}

	@Override
	public void buttonPressed(Button button) {
		
		byte[] publicKey = hexStringToByteArray(pubKey.getText());
		byte[] privateKey = hexStringToByteArray(privKey.getText());
		byte[] credential = hexStringToByteArray(credent.getText());
		byte[] vote = list.getSelectedItem().toString().getBytes();
		
		Decryptor d = new Decryptor();
		Encryptor e = d.getEncryptor();
		// TODO: Decryptor.setPrivateKey etc
		Encryptor se = new Decryptor().getEncryptor();
		
		
		Voter voter = new Voter(d, credential, se, vote);
		try {
			voter.vote();
		} catch (NetworkError e1) {
			e1.printStackTrace();
		}
		
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	@Override
	public void selectedItemChanged(ListView arg0, Object arg1) {
		vote.setText(list.getSelectedItem().toString());
	}

	@Override
	public void selectedRangeAdded(ListView arg0, int arg1, int arg2) {
	}

	@Override
	public void selectedRangeRemoved(ListView arg0, int arg1, int arg2) {
	}

	@Override
	public void selectedRangesChanged(ListView arg0, Sequence<Span> arg1) {
	}

}
