/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This code originates from Jiří Schejbal's master thesis. Jiří Schejbal
 * is also the author of the original version of this code.
 * With his approval, we use his code in jInfer and we slightly modify it to
 * suit our cause.
 */
package cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the content of a constructor.
 *
 * @author Jiri Schejbal
 */
public class Content {

    private List<ContentItem> items = new ArrayList<ContentItem>();

    public Content(ContentItem item) {
        add(item);
    }

    public void add(ContentItem item) {
        items.add(item);
    }

    public XQNodeList<XQNode> getNodes() {
        XQNodeList<XQNode> result = new XQNodeList<XQNode>();
        StringContent stringContent = new StringContent();
        for (ContentItem item : items) {
            item.handleContent(result, stringContent);
        }
        ContentItem.flushStringContent(result, stringContent);
        return result;
    }

}