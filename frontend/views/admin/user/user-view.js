import {PolymerElement} from '@polymer/polymer/polymer-element';
import {html} from '@polymer/polymer/lib/utils/html-tag';
import '@vaadin/vaadin-split-layout';
import '@vaadin/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-column';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';

class UserView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`
            <vaadin-split-layout style="width: 100%; height: 100%;">
                <div style="flex-grow:1;width:100%;" id="grid-wrapper">
                    <vaadin-grid id="grid"></vaadin-grid>
                </div>
                <div style="width:400px;display:flex;flex-direction:column;">
                    <div style="padding:var(--lumo-space-l);flex-grow:1;">
                        <vaadin-form-layout>
                            <vaadin-text-field label="Name" id="name"></vaadin-text-field>
                            <vaadin-email-field label="Email" id="email"></vaadin-email-field>
                            <vaadin-text-field label="Phone" id="phone"></vaadin-text-field>
                        </vaadin-form-layout>
                    </div>
                    <vaadin-horizontal-layout
                            style="flex-wrap:wrap;width:100%;background-color:var(--lumo-contrast-5pct);padding:var(--lumo-space-s) var(--lumo-space-l);"
                            theme="spacing"
                    >
                        <vaadin-button theme="primary" id="enter">Save</vaadin-button>
                        <vaadin-button theme="tertiary" slot="" id="clear">Cancel</vaadin-button>
                    </vaadin-horizontal-layout>
                </div>
            </vaadin-split-layout>
        `;
    }

    static get is() {
        return 'user-view';
    }
}

customElements.define(UserView.is, UserView);

