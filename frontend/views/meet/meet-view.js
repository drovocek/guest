import {PolymerElement} from '@polymer/polymer/polymer-element';
import {html} from '@polymer/polymer/lib/utils/html-tag';

import '@polymer/iron-icon/iron-icon';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-lumo-styles/all-imports';
import '@vaadin/vaadin-icons';

class MeetView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`
            <vaadin-vertical-layout id="rootLayout" style="width: 100%; height: 100%;">
                <vaadin-vertical-layout id="cardLayout"></vaadin-vertical-layout>
            </vaadin-vertical-layout>
        `;
    }

    static get is() {
        return 'meet-view';
    }
}

customElements.define(MeetView.is, MeetView);




