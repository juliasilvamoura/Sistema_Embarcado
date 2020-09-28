package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends IOIOActivity {
	private ImageButton next;
	private ImageButton back;
	private ImageButton left;
	private ImageButton right;
	private Boolean estadoNext;
	private Boolean estadoBack;
	private Boolean estadoLeft;
	private Boolean estadoRight;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		next = (ImageButton) findViewById(R.id.next);
		back = (ImageButton) findViewById(R.id.back);
		left = (ImageButton) findViewById(R.id.left);
		right = (ImageButton) findViewById(R.id.right);
		configuraBotoes();
	}

	@SuppressLint("ClickableViewAccessibility")
	public void configuraBotoes() {
		estadoNext = false;
		estadoBack = false;
		estadoLeft = false;
		estadoRight = false;


		next.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					estadoNext = true;
					exibirMensagem("Frente");
					return  true;
				}
				if(event.getAction() == MotionEvent.ACTION_UP) {
					estadoNext = false;
					exibirMensagem("Parado");
					return true;
				}
				return  false;
			}
		});

		back.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					estadoBack = true;
					exibirMensagem("Tras");
					return  true;
				}
				if(event.getAction() == MotionEvent.ACTION_UP) {
					estadoBack = false;
					exibirMensagem("Parado");
					return true;
				}
				return  false;
			}
		});

		left.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					estadoLeft = true;
					exibirMensagem("Esquerda");
					return  true;
				}
				if(event.getAction() == MotionEvent.ACTION_UP) {
					estadoLeft = false;
					exibirMensagem("Parado");
					return true;
				}
				return  false;
			}
		});

		right.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					estadoRight = true;
					exibirMensagem("Direita");
					return  true;
				}
				if(event.getAction() == MotionEvent.ACTION_UP) {
					estadoRight = false;
					exibirMensagem("Parado");
					return true;
				}
				return  false;
			}
		});
	}

	public void exibirMensagem(String mensagem) {
		Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
	}

	class Looper extends BaseIOIOLooper {
		private PwmOutput motor1;
		private PwmOutput motor2;
		private DigitalOutput in1_motor1;
		private DigitalOutput in2_motor1;
		private DigitalOutput in1_motor2;
		private DigitalOutput in2_motor2;

		@Override
		protected void setup() throws ConnectionLostException {
			in1_motor1 = ioio_.openDigitalOutput(1, false);
			in2_motor1 = ioio_.openDigitalOutput(2, false);
			in1_motor2 = ioio_.openDigitalOutput(3, false);
			in2_motor2 = ioio_.openDigitalOutput(4, false);
			motor1 = ioio_.openPwmOutput(5, 100);
			motor2 = ioio_.openPwmOutput(6, 100);
		}

		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			if(estadoNext) {
				in1_motor1.write(true);
				in2_motor1.write(false);
				in1_motor2.write(true);
				in2_motor2.write(false);

				motor1.setDutyCycle(1.0f);
				motor2.setDutyCycle(1.0f);

			} else if (estadoBack) {
				in1_motor1.write(false);
				in2_motor1.write(true);
				in1_motor2.write(false);
				in2_motor2.write(true);

				motor1.setDutyCycle(1.0f);
				motor2.setDutyCycle(1.0f);

			} else if (estadoLeft) {
				in1_motor1.write(true);
				in2_motor1.write(false);
				in1_motor2.write(false);
				in2_motor2.write(true);

				motor1.setDutyCycle(0.5f);
				motor2.setDutyCycle(0.5f);

			} else if (estadoRight) {
				in1_motor1.write(false);
				in2_motor1.write(true);
				in1_motor2.write(true);
				in2_motor2.write(false);

				motor1.setDutyCycle(0.5f);
				motor2.setDutyCycle(0.5f);

			} else {
				in1_motor1.write(false);
				in2_motor1.write(false);
				in1_motor2.write(false);
				in2_motor2.write(false);

				motor1.setDutyCycle(0.0f);
				motor2.setDutyCycle(0.0f);
			}
		}

		@Override
		public void disconnected() {
			toast("IOIO disconnected");
		}
}
	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}


	private void toast(final String message) {
		final Context context = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});
	}

}
