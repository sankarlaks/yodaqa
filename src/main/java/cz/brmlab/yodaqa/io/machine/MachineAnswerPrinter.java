package cz.brmlab.yodaqa.io.machine;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import cz.brmlab.yodaqa.model.AnswerHitlist.Answer;

/**
 * A trivial consumer that will extract the final answer and print it
 * on the standard output.
 *
 * Pair this with MachineQuestionReader.
 */

public class MachineAnswerPrinter extends JCasConsumer_ImplBase {

	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
	}

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		JCas answerHitlist;
		try {
			answerHitlist = jcas.getView("AnswerHitlist");
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
		FSIndex idx = answerHitlist.getJFSIndexRepository().getIndex("SortedAnswers");
		FSIterator answers = idx.iterator();
		if (answers.hasNext()) {
			int i = 1;
			while (answers.hasNext() && i <= 4) {
				Answer answer = (Answer) answers.next();
				if (i > 1)
					System.out.print("  |  ");
				System.out.print(answer.getText() + " (" + String.format("%.2f", answer.getConfidence()) + ")");
				i++;
			}
			System.out.println();
		} else {
			System.out.println("No answer found.");
		}
	}
}
