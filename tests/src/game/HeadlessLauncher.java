/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package game;
import com.game.Main;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class HeadlessLauncher extends BlockJUnit4ClassRunner implements ApplicationListener {

	public HeadlessLauncher(Class<?> c) throws InitializationError {
		super(c);
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		Gdx.gl = mock(GL20.class);
		new HeadlessApplication(new Main(), conf);

	}


	@Override
	public void create() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}




}
